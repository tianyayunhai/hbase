/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hbase.io.compress;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.io.Closeable;
import java.io.IOException;
import org.apache.hadoop.hbase.nio.ByteBuff;
import org.apache.yetus.audience.InterfaceAudience;

/**
 * Specification of a ByteBuff-based decompressor, which can be more efficient than the stream-based
 * Decompressor.
 */
@InterfaceAudience.Private
public interface ByteBuffDecompressor extends Closeable {

  /**
   * Fills the ouput buffer with uncompressed data. Always call
   * {@link #canDecompress(ByteBuff, ByteBuff)} first to check if this decompressor can handle your
   * input and output buffers.
   * @return The actual number of bytes of uncompressed data.
   */
  int decompress(ByteBuff output, ByteBuff input, int inputLen) throws IOException;

  /**
   * Signals of these two particular {@link ByteBuff}s are compatible with this decompressor.
   * ByteBuffs can have one or multiple backing buffers, and each of these may be stored in heap or
   * direct memory. Different {@link ByteBuffDecompressor}s may be able to handle different
   * combinations of these, so always check.
   */
  boolean canDecompress(ByteBuff output, ByteBuff input);

  /**
   * Call before every use of {@link #canDecompress(ByteBuff, ByteBuff)} and
   * {@link #decompress(ByteBuff, ByteBuff, int)} to reinitialize the decompressor with settings
   * from the HFileInfo. This can matter because ByteBuffDecompressors are reused many times.
   */
  void reinit(@Nullable Compression.HFileDecompressionContext newHFileDecompressionContext);

}
