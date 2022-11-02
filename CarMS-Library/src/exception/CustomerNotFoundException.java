/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author wayneonn
 */
public class CustomerNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>OutletNotFoundException</code> without
     * detail message.
     */
    public CustomerNotFoundException() {
    }

    /**
     * Constructs an instance of <code>OutletNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomerNotFoundException(String msg) {
        super(msg);
    }
}
