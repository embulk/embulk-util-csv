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
 * Thrown when {@link CsvTokenizer} encounters an unexpected extra trailing column (i.e. too many columns).
 */
public class RecordHasUnexpectedTrailingColumnException extends InvalidCsvFormatException {
    /**
     * Constructs a {@link RecordHasUnexpectedTrailingColumnException} with its default message.
     */
    public RecordHasUnexpectedTrailingColumnException() {
        super("A record has an unexpected trailing column (i.e. too many columns).");
    }
}
