/*
 * Copyright 2021 The Embulk project
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

public final class UnexpectedCharacterAfterQuoteException extends Exception {
    public UnexpectedCharacterAfterQuoteException(final char unexpected, final char quote) {
        super(String.format("CsvTokenizer reached at an unexpected extra character '%c' after a quoted field by '%c'", unexpected, quote));
        this.unexpected = unexpected;
        this.quote = quote;
    }

    char getUnexpected() {
        return this.unexpected;
    }

    char getQuote() {
        return this.quote;
    }

    private final char unexpected;
    private final char quote;
}
