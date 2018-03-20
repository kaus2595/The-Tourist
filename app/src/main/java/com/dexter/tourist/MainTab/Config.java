/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dexter.tourist.MainTab;

public abstract class Config {
    // copy this keys from your developer dashboard
    public static final String ACCESS_TOKEN = "62e6f6b827d04e51b19660a051f95e4a";

    public static final LanguageConfig[] languages = new LanguageConfig[]{
            new LanguageConfig("en", "a11ea1d839e3446d84e402cb97cdadfb"),
            new LanguageConfig("ru", "c8acebfbeeaa42ccb986e30573509055"),
            new LanguageConfig("de", "ae2afb2dfd3f4a02bb0da9dd32b78ff6"),
            new LanguageConfig("pt", "b27372e24ee44db48df4dccbd57ea021"),
    };

    public static final String[] events = new String[]{
            "hello_event",
            "goodbye_event",
            "how_are_you_event"
    };
}
