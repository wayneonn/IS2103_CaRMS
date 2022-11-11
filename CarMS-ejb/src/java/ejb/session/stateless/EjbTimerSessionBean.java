/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Cars;
import entity.Outlet;
import entity.ReservationRecord;
import enumerations.CarStateEnumeration;
import exception.OutletNotFoundException;
import exception.RentalReservationNotFoundException;
import java.util.Date;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wayneonn
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {
    
    private static boolean ALLOCATED = true;

    @EJB
    private TransitDriverDispatchRecordSessionBeanLocal transitDriverDispatchRecordSessionBean;

    @EJB
    private CarSessionBeanLocal carSessionBean;

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    //Retrieve a list of all car rental reservations for pickup on the current date and allocate an available car for the reserved car (make and) model or category.
    //When allocating cars, priority should be accorded to cars that are already in the pickup outlet or will be returned to the pickup outlet in time.
    //Cars that are at a different outlet from the pickup outlet should be allocated only when necessary.
    @Schedule(hour = "*", minute = "*", second = "*/10")
    @Override
    public void activateAllocation() {
        Date todayDate = new Date();
        System.out.println("allocateCarsToCurrentDayReservations event: " + new Date());
        allocateCarsToCurrentDayReservations(todayDate);
    }

    private Calendar dateToCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }

    @Override
    public void allocateCarsToCurrentDayReservations(Date todayDate) {
        Date startDate = todayDate;
        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);
        EjbTimerSessionBean obj = new EjbTimerSessionBean();
        Calendar calendar = obj.dateToCalendar(startDate);
        calendar.add(Calendar.DATE, 1);
        Date endDate = calendar.getTime();
        System.out.println("Current Date event: " + startDate);
        System.out.println("Next Date event: " + endDate);
        Query query = em.createQuery("SELECT r FROM ReservationRecord r WHERE r.pickupDateTime >= :inStartDate AND r.pickupDateTime <= :inEndDate");
        query.setParameter("inStartDate", startDate);
        query.setParameter("inEndDate", endDate);
        List<ReservationRecord> reservationsToBeAllocated = query.getResultList();
        List<ReservationRecord> reservationsThatRequiresTransit = new ArrayList<>();

        for (ReservationRecord reservationRecord : reservationsToBeAllocated) {
            boolean alreadyAllocated = false;
            if (reservationRecord.getCategoryCriteria() != null) { // rental reservation by category
                List<Cars> cars = carSessionBean.retrieveCarsByCategoryId(reservationRecord.getCategoryCriteria().getCategoryId());
                // first sifts out those cars that are not needed for transit 
                for (Cars car : cars) {
                    if (car.getModel().getCategory().getCategoryName().equals(reservationRecord.getCategoryCriteria().getCategoryName()) // car category and reservation record matches
                            && car.getReservationRecord() == null // car reservation record is empty
                            && car.getOutlet().getOutletId().equals(reservationRecord.getPickupOutlet().getOutletId())) { //car outlet location is equals to reservation record pickup location
                        reservationRecord.setCar(car);
                        car.setReservationRecord(reservationRecord);
                        alreadyAllocated = ALLOCATED;
                        break;
                    }
                }
                if (alreadyAllocated) {
                    continue;
                }

                // breaks the loop if the car are available and reservationRecord is empty
                Long carCategoryId = reservationRecord.getCategoryCriteria().getCategoryId();
                List<Cars> carsOfSameCategory = carSessionBean.retrieveCarsByCategoryId(carCategoryId); // find list of cars that matches at the outlet
                for (Cars car : carsOfSameCategory) {
                    CarStateEnumeration carCurrentState = car.getCarState();
                    if ((carCurrentState == CarStateEnumeration.AVAILABLE) && car.getReservationRecord() == null) { // already available in outlet
                        reservationRecord.setCar(car);
                        car.setReservationRecord(reservationRecord);
                        alreadyAllocated = ALLOCATED;
                        reservationsThatRequiresTransit.add(reservationRecord);
                        break;
                    }
                }
                if (alreadyAllocated) {
                    continue;
                }

//                // check returning cars to same outlet
//                for (Cars car : carsOfSameCategory) {
//                    CarStateEnumeration carCurrentState = car.getCarState();
//                    String carReservationReturnOutlet = car.getReservationRecord().getReturnOutlet().getOutletName();
//                    String reservationReturnOutlet = reservationRecord.getPickupOutlet().getOutletName();
//                    if ((carCurrentState == CarStateEnumeration.ONRENTAL) && carReservationReturnOutlet.equals(reservationReturnOutlet)) {
//                        Date carReturnDate = car.getReservationRecord().getReturnDateTime();
//                        Date reservationReturnDate = reservationRecord.getPickupDateTime();
//                        if (carReturnDate.before(reservationReturnDate)) {
//                            reservationRecord.setCar(car);
//                            alreadyAllocated = ALLOCATED;
//                            break;
//                        }
//                    }
//                }
//                if (alreadyAllocated) {
//                    continue;
//                }
                //  checking cars on rental and returning to the same outlet within 2 hours
                for (Cars car : carsOfSameCategory) {
                    CarStateEnumeration carCurrentState = car.getCarState();
                    Outlet carReservationReturnOutlet = car.getReservationRecord().getReturnOutlet();
                    Outlet reservationReturnOutlet = reservationRecord.getPickupOutlet();
                    if ((carCurrentState == CarStateEnumeration.ONRENTAL) && carReservationReturnOutlet.equals(reservationReturnOutlet)) {
                        Calendar onRentalAfterTwoHours = obj.dateToCalendar(startDate);
                        calendar.add(Calendar.HOUR, 2);
                        Date onRentalAfterTwoHour = onRentalAfterTwoHours.getTime();
                        if (reservationRecord.getPickupDateTime().after(onRentalAfterTwoHour)) {
                            reservationRecord.setCar(car);
                            alreadyAllocated = ALLOCATED;
                            reservationsThatRequiresTransit.add(reservationRecord);
                            break;
                        }
                    }
                }
                if (alreadyAllocated) {
                    continue;
                }

            } else {
                List<Cars> cars = carSessionBean.retrieveCarsByModelId(reservationRecord.getModelCriteria().getModelId());
                // first sifts out those cars that are not needed for transit 
                for (Cars car : cars) {
                    CarStateEnumeration carCurrentState = car.getCarState();
                    if ((carCurrentState == CarStateEnumeration.AVAILABLE && car.getReservationRecord() == null)
                            && car.getOutlet().getOutletId().equals(reservationRecord.getPickupOutlet().getOutletId())) {
                        reservationRecord.setCar(car);
                        car.setReservationRecord(reservationRecord);
                        alreadyAllocated = ALLOCATED;
                        break;
                    }
                }
                if (alreadyAllocated) {
                    continue;
                }

                // breaks the loop if the car are available and reservationRecord is empty
                for (Cars car : cars) {
                    CarStateEnumeration carCurrentState = car.getCarState();
                    if (carCurrentState == CarStateEnumeration.AVAILABLE && car.getReservationRecord() == null) {
                        reservationRecord.setCar(car);
                        car.setReservationRecord(reservationRecord);
                        alreadyAllocated = ALLOCATED;
                        reservationsThatRequiresTransit.add(reservationRecord);
                        break;
                    }
                }
                if (alreadyAllocated) {
                    continue;
                }

//                // check returning cars to same outlet
//                for (Cars car : cars) {
//                    CarStateEnumeration carCurrentState = car.getCarState();
//                    String carReservationReturnOutlet = "";
//                    String reservationReturnOutlet = "";
//
//                    if (car.getReservationRecord() != null) {
//                        carReservationReturnOutlet = car.getReservationRecord().getReturnOutlet().getOutletName();
//                        reservationReturnOutlet = reservationRecord.getPickupOutlet().getOutletName();
//                    }
//
//                    if ((carCurrentState == CarStateEnumeration.ONRENTAL && car.getReservationRecord() == null)
//                            && carReservationReturnOutlet.equals(reservationReturnOutlet)) {
//                        if (car.getReservationRecord() != null && car.getReservationRecord().getReturnDateTime().before(reservationRecord.getPickupDateTime())) {
//                            reservationRecord.setCar(car);
//                            alreadyAllocated = ALLOCATED;
//                            break;
//                        }
//                    }
//                }
//                if (alreadyAllocated) continue;
                //  checking cars on rental and returning to the same outlet within 2 hours
                for (Cars car : cars) {
                    CarStateEnumeration carCurrentState = car.getCarState();
                    Outlet carReservationReturnOutlet = new Outlet();
                    Outlet reservationReturnOutlet = new Outlet();

                    if (car.getReservationRecord() != null) {
                        carReservationReturnOutlet = car.getReservationRecord().getReturnOutlet();
                        reservationReturnOutlet = reservationRecord.getPickupOutlet();
                    }
                    if ((carCurrentState == CarStateEnumeration.ONRENTAL) && car.getReservationRecord() == null
                            && carReservationReturnOutlet.equals(reservationReturnOutlet)) {
                        Calendar onRentalAfterTwoHours = obj.dateToCalendar(startDate);
                        onRentalAfterTwoHours.add(Calendar.HOUR, 2);
                        Date transitEndTime = onRentalAfterTwoHours.getTime();
                        if (car.getReservationRecord() != null && reservationRecord.getPickupDateTime().after(transitEndTime)) {
                            reservationRecord.setCar(car);
                            alreadyAllocated = ALLOCATED;
                            reservationsThatRequiresTransit.add(reservationRecord);
                            break;
                        }
                    }
                }
                if (alreadyAllocated) {
                    continue;
                }
            }
        }
        System.out.println("reservationsThatRequiresTransit" + reservationsThatRequiresTransit);
    }

    public void generateTransitDriverDispatchRecords(Date date, List<ReservationRecord> rentalReservationsToBeAllocated) {
        try {
            for (ReservationRecord rentalReservation : rentalReservationsToBeAllocated) {
                Date transitStartDate = date;

                transitDriverDispatchRecordSessionBean.createNewTranspatchDriverRecordCommit(rentalReservation.getPickupOutlet().getOutletId(),
                        rentalReservation.getReservationId(), transitStartDate);
            }
        } catch (RentalReservationNotFoundException | OutletNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
