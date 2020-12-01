/*
 * Copyright (c) 2008-2020, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package usercodedeployment;

public class CustomExceptions {

    public static class CustomException extends Exception {
        public CustomException() {

        }
    }

    public static class CustomExceptionWithMessage extends Exception {
        public CustomExceptionWithMessage(String message) {
            super(message);
        }
    }

    public static class CustomExceptionWithMessageAndCause extends Exception {
        public CustomExceptionWithMessageAndCause(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class CustomExceptionWithCause extends Exception {
        public CustomExceptionWithCause(Throwable cause) {
            super(cause);
        }
    }

    public static class CustomExceptionNonStandardSignature extends Throwable {
        public CustomExceptionNonStandardSignature(int i) {
        }
    }
}