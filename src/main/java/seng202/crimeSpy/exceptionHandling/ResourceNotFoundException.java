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
 * Exception: Could not find the resource
 * <p>Exception will typically be caused by attempting to access or open a file that does
 * not exist or otherwise is not in the path specified</p>
 */
public class ResourceNotFoundException extends Exception {

        // Empty Constructor
        public ResourceNotFoundException() {
            super("The resource url could not be located");
        }

        // Constructor that accepts a message
        public ResourceNotFoundException(String message) {
            super(message);
        }
}
