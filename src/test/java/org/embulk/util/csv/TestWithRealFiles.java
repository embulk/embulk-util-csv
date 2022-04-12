/*
 * Copyright 2017 The Embulk project
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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.embulk.spi.Buffer;
import org.embulk.spi.FileInput;
import org.embulk.util.text.LineDecoder;
import org.embulk.util.text.LineDelimiter;
import org.junit.Test;

public class TestWithRealFiles {
    @Test
    public void testSimple() throws Exception {
        final LineDecoder input = LineDecoder.of(
                new ResourceFileInput("test_simple.csv"), StandardCharsets.UTF_8, LineDelimiter.LF);

        final CsvTokenizer.Builder tokenizerBuilder = CsvTokenizer.builder(",")
                .setNewline("\n")
                .setQuote('\"')
                .setEscape('\"')
                .setNullString("NULL");

        final List<List<String>> actualRecords = tokenizeAll(tokenizerBuilder, input);

        final List<List<String>> expectedRecords = Arrays.asList(
                Arrays.asList("id", "account", "time", "purchase", "comment"),
                Arrays.asList("1", "32864", "2015-01-27 19:23:49", "20150127", "embulk"),
                Arrays.asList("2", "14824", "2015-01-27 19:01:23", "20150127", "embulk jruby"),
                Arrays.asList("3", "27559", "2015-01-28 02:20:02", "20150128", "Embulk \"csv\" parser plugin"),
                Arrays.asList("4", "11270", "2015-01-29 11:54:36", "20150129", null)
        );
        assertEquals(expectedRecords, actualRecords);
    }

    @Test
    public void changePreviewSampleBufferBytes() throws Exception {
        final LineDecoder input = LineDecoder.of(
                new ResourceFileInput("test_sample_buffer_bytes.csv"), StandardCharsets.UTF_8, LineDelimiter.LF);

        final CsvTokenizer.Builder tokenizerBuilder = CsvTokenizer.builder(",")
                .setNewline("\n")
                .setQuote('\"')
                .setEscape('\"')
                .setNullString("NULL");

        final List<List<String>> actualRecords = tokenizeAll(tokenizerBuilder, input);

        final List<List<String>> expectedRecords = Arrays.asList(
                Arrays.asList("id", "account", "time", "purchase", "comment"),
                Arrays.asList("1", "32864", "2015-01-27 19:23:49", "20150127", "embulk"),
                Arrays.asList("2", "14824", "2015-01-27 19:01:23", "20150127", "embulk jruby"),
                Arrays.asList("3", "27559", "2015-01-28 02:20:02", "20150128", "Embulk \"csv\" parser plugin"),
                Arrays.asList("4", "11270", "2015-01-29 11:54:36", "20150129", null)
        );
        assertEquals(expectedRecords, actualRecords);
    }

    private static List<List<String>> tokenizeAll(final CsvTokenizer.Builder tokenizerBuilder, final LineDecoder input) {
        final ArrayList<List<String>> records = new ArrayList<>();
        while (input.nextFile()) {
            final CsvTokenizer tokenizer = tokenizerBuilder.build(input.iterator());
            if (!tokenizer.nextRecord()) {
                // empty file
                continue;
            }

            while (true) {
                boolean hasNextRecord = true;

                try {
                    final ArrayList<String> record = new ArrayList<>();
                    while (tokenizer.hasNextColumn()) {
                        final String field = tokenizer.nextColumnOrNull();
                        record.add(field);
                    }

                    try {
                        hasNextRecord = tokenizer.nextRecord();
                    } catch (final RecordHasUnexpectedTrailingColumnException ex) {
                        throw ex;
                    }
                    records.add(Collections.unmodifiableList(record));
                } catch (final RecordDoesNotHaveExpectedColumnException | RecordHasUnexpectedTrailingColumnException ex) {
                    throw ex;
                }

                if (!hasNextRecord) {
                    break;
                }
            }
        }
        return Collections.unmodifiableList(records);
    }

    private static class ResourceFileInput implements FileInput {
        ResourceFileInput(final String resourceName) {
            this.hasPolled = false;
            this.hasFilePointed = false;
            try {
                final Path resourcePath = Paths.get(TestWithRealFiles.class.getClassLoader().getResource(resourceName).getFile());
                this.buffer = MockBuffer.copyOf(Files.readAllBytes(resourcePath));
            } catch (final IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }

        @Override
        public boolean nextFile() {
            if (this.hasFilePointed) {
                return false;
            }
            this.hasFilePointed = true;
            return true;
        }

        @Override
        public Buffer poll() {
            if ((!this.hasFilePointed) || this.hasPolled) {
                return null;
            }
            this.hasPolled = true;
            return this.buffer;
        }

        @Override
        public void close() {
        }

        private final Buffer buffer;

        private boolean hasFilePointed;
        private boolean hasPolled;
    }
}
