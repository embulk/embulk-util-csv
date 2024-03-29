/*
 * Copyright 2014 The Embulk project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.embulk.util.csv;

/**
 * Thrown when {@link CsvTokenizer} encounters an invalid format while parsing a quoted field.
 *
 * <p>It corresponds to {@code CsvTokenizer.InvalidValueException} in the original {@code embulk-standards}.
 */
public abstract class InvalidCsvQuotationException extends InvalidCsvFormatException {
    /**
     * Constructs an {@link InvalidCsvQuotationException} with the specified detail message.
     *
     * @param message  the detail message
     */
    public InvalidCsvQuotationException(final String message) {
        super(message);
    }
}
