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
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.embulk.config.ConfigSource;
import org.embulk.parser.csv.CsvParserPlugin;
import org.embulk.parser.csv.CsvTokenizer;
import org.embulk.spi.Buffer;
import org.embulk.spi.FileInput;
import org.embulk.util.config.ConfigMapperFactory;
import org.embulk.util.text.LineDecoder;
import org.embulk.util.text.LineDelimiter;
import org.junit.Test;

public class TestWithRealFiles {
    @Test
    public void testSimple() throws Exception {
        final LineDecoder input = LineDecoder.of(
                new ResourceFileInput("test_simple.csv"), StandardCharsets.UTF_8, LineDelimiter.LF);

        final ConfigSource config = CONFIG_MAPPER_FACTORY.newConfigSource();
        config.set("charset", "UTF-8");
        config.set("newline", "LF");
        config.set("type", "csv");
        config.set("delimiter", ",");
        config.set("quote", "\"");
        config.set("escape", "\"");
        config.set("null_string", "NULL");
        config.set("trim_if_not_quoted", false);
        config.set("skip_header_lines", 1);
        config.set("allow_extra_columns", false);
        config.set("allow_optional_columns", false);
        final ConfigSource column0 = CONFIG_MAPPER_FACTORY.newConfigSource();
        column0.set("name", "id");
        column0.set("type", "long");
        final ConfigSource column1 = CONFIG_MAPPER_FACTORY.newConfigSource();
        column1.set("name", "account");
        column1.set("type", "long");
        final ConfigSource column2 = CONFIG_MAPPER_FACTORY.newConfigSource();
        column2.set("name", "time");
        column2.set("type", "timestamp");
        column2.set("format", "%Y-%m-%d %H:%M:%S");
        final ConfigSource column3 = CONFIG_MAPPER_FACTORY.newConfigSource();
        column3.set("name", "purchase");
        column3.set("type", "timestamp");
        column3.set("format", "%Y%m%d");
        final ConfigSource column4 = CONFIG_MAPPER_FACTORY.newConfigSource();
        column4.set("name", "comment");
        column4.set("type", "string");
        config.set("columns", Arrays.asList(column0, column1, column2, column3, column4));
        final CsvParserPlugin.PluginTask task =
                CONFIG_MAPPER_FACTORY.createConfigMapper().map(config, CsvParserPlugin.PluginTask.class);

        final CsvTokenizer tokenizer = new CsvTokenizer(input, task);
        final List<List<String>> actualRecords = tokenizeAll(tokenizer);

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

        final ConfigSource config = CONFIG_MAPPER_FACTORY.newConfigSource();
        config.set("charset", "UTF-8");
        config.set("newline", "LF");
        config.set("type", "csv");
        config.set("delimiter", ",");
        config.set("quote", "\"");
        config.set("escape", "\"");
        config.set("null_string", "NULL");
        config.set("trim_if_not_quoted", false);
        config.set("skip_header_lines", 1);
        config.set("allow_extra_columns", false);
        config.set("allow_optional_columns", false);
        final ConfigSource column0 = CONFIG_MAPPER_FACTORY.newConfigSource();
        column0.set("name", "id");
        column0.set("type", "long");
        final ConfigSource column1 = CONFIG_MAPPER_FACTORY.newConfigSource();
        column1.set("name", "account");
        column1.set("type", "long");
        final ConfigSource column2 = CONFIG_MAPPER_FACTORY.newConfigSource();
        column2.set("name", "time");
        column2.set("type", "timestamp");
        column2.set("format", "%Y-%m-%d %H:%M:%S");
        final ConfigSource column3 = CONFIG_MAPPER_FACTORY.newConfigSource();
        column3.set("name", "purchase");
        column3.set("type", "timestamp");
        column3.set("format", "%Y%m%d");
        final ConfigSource column4 = CONFIG_MAPPER_FACTORY.newConfigSource();
        column4.set("name", "comment");
        column4.set("type", "string");
        config.set("columns", Arrays.asList(column0, column1, column2, column3, column4));
        final CsvParserPlugin.PluginTask task =
                CONFIG_MAPPER_FACTORY.createConfigMapper().map(config, CsvParserPlugin.PluginTask.class);

        final CsvTokenizer tokenizer = new CsvTokenizer(input, task);
        final List<List<String>> actualRecords = tokenizeAll(tokenizer);

        final List<List<String>> expectedRecords = Arrays.asList(
                Arrays.asList("id", "account", "time", "purchase", "comment"),
                Arrays.asList("1", "32864", "2015-01-27 19:23:49", "20150127", "embulk"),
                Arrays.asList("2", "14824", "2015-01-27 19:01:23", "20150127", "embulk jruby"),
                Arrays.asList("3", "27559", "2015-01-28 02:20:02", "20150128", "Embulk \"csv\" parser plugin"),
                Arrays.asList("4", "11270", "2015-01-29 11:54:36", "20150129", null)
        );
        assertEquals(expectedRecords, actualRecords);
    }

    private static List<List<String>> tokenizeAll(final CsvTokenizer tokenizer) {
        final ArrayList<List<String>> records = new ArrayList<>();
        while (tokenizer.nextFile()) {
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
                    } catch (final CsvTokenizer.TooManyColumnsException ex) {
                        throw ex;
                    }
                    records.add(Collections.unmodifiableList(record));
                } catch (final CsvTokenizer.InvalidFormatException | CsvTokenizer.InvalidValueException ex) {
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

    private static final ConfigMapperFactory CONFIG_MAPPER_FACTORY = ConfigMapperFactory.builder().addDefaultModules().build();
}
