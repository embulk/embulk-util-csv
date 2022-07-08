/*
 * Copyright 2022 The Embulk project
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
 * Thrown when {@link CsvTokenizer} encounters an unexpected character after parsing a quoted field.
 */
public class InvalidCharacterAfterQuoteException extends InvalidCsvQuotationException {
    /**
     * Constructs an {@link InvalidCharacterAfterQuoteException} with its default message.
     *
     * @param extraChar  the unexpected character after a quoted field
     * @param quoteChar  the quote character
     */
    public InvalidCharacterAfterQuoteException(final char extraChar, final char quoteChar) {
        super(String.format("Unexpected extra character '%c' after a quote by '%c'.", extraChar, quoteChar));
        this.extraChar = extraChar;
        this.quoteChar = quoteChar;
    }

    private final char extraChar;
    private final char quoteChar;
}
