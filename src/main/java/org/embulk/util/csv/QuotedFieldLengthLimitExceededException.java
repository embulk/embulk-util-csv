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
 * Thrown when {@link CsvTokenizer} encounters a quoted field which is too long.
 */
public class QuotedFieldLengthLimitExceededException extends InvalidCsvQuotationException {
    /**
     * Constructs a {@link QuotedFieldLengthLimitExceededException} with its default message.
     *
     * @param quotedFieldLengthLimit  the limit of a quoted field length
     */
    public QuotedFieldLengthLimitExceededException(final long quotedFieldLengthLimit) {
        super("The length of the quoted field exceeds the limit (" + quotedFieldLengthLimit + ")");
        this.quotedFieldLengthLimit = quotedFieldLengthLimit;
    }

    private final long quotedFieldLengthLimit;
}
