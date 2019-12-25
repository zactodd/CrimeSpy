/*
    crimeSpy is a FOSS crime analysis software.
    Copyright (C) 2015 SENG Team Supreme

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package seng202.crimeSpy.exceptionHandling;

/**
 * Exception: unique crimeID number violated
 * <p>Exception will typically be caused by attempting to add a non unique
 * Crime to a crime collection.</p>
 */
public class NonUniqueCrimeIDException extends Exception{

        // Empty Constructor
        public NonUniqueCrimeIDException() {
            super("A non unique crimeID was identified");
        }

        // Constructor that accepts a message
        public NonUniqueCrimeIDException(String message) {
            super(message);
        }
}
