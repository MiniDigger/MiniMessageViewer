/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.text.minimessage.transformation

import net.kyori.adventure.text.minimessage.transformation.inbuild.ClickTransformation
import net.kyori.adventure.text.minimessage.transformation.inbuild.ColorTransformation
import net.kyori.adventure.text.minimessage.transformation.inbuild.DecorationTransformation
import net.kyori.adventure.text.minimessage.transformation.inbuild.FontTransformation
import net.kyori.adventure.text.minimessage.transformation.inbuild.GradientTransformation
import net.kyori.adventure.text.minimessage.transformation.inbuild.HoverTransformation
import net.kyori.adventure.text.minimessage.transformation.inbuild.InsertionTransformation
import net.kyori.adventure.text.minimessage.transformation.inbuild.KeybindTransformation
import net.kyori.adventure.text.minimessage.transformation.inbuild.RainbowTransformation
import net.kyori.adventure.text.minimessage.transformation.inbuild.TranslatableTransformation


/**
 * Available types of transformation.
 *
 * @param <T> transformation class
 * @since 4.1.0
</T> */
class TransformationType<T : Transformation> internal constructor(canParse: (String) -> Boolean, parser: TransformationParser<T>) {
  val canParse: (String) -> Boolean
  val parser: TransformationParser<T>

  companion object {
    val COLOR: TransformationType<ColorTransformation> = TransformationType({ ColorTransformation.canParse(it) }, ColorTransformation.Parser())
    val DECORATION: TransformationType<DecorationTransformation> = TransformationType({ DecorationTransformation.canParse(it) }, DecorationTransformation.Parser())
    val HOVER_EVENT: TransformationType<HoverTransformation> = TransformationType({ HoverTransformation .canParse(it) }, HoverTransformation.Parser())
    val CLICK_EVENT: TransformationType<ClickTransformation> = TransformationType({ ClickTransformation .canParse(it) }, ClickTransformation.Parser())
    val KEYBIND: TransformationType<KeybindTransformation> = TransformationType({ KeybindTransformation .canParse(it) }, KeybindTransformation.Parser())
    val TRANSLATABLE: TransformationType<TranslatableTransformation> = TransformationType({ TranslatableTransformation .canParse(it) }, TranslatableTransformation.Parser())
    val INSERTION: TransformationType<InsertionTransformation> = TransformationType({ InsertionTransformation .canParse(it) }, InsertionTransformation.Parser())
    val FONT: TransformationType<FontTransformation> = TransformationType({ FontTransformation .canParse(it) }, FontTransformation.Parser())
    val GRADIENT: TransformationType<GradientTransformation> = TransformationType({ GradientTransformation .canParse(it) }, GradientTransformation.Parser())
    val RAINBOW: TransformationType<RainbowTransformation> = TransformationType({ RainbowTransformation .canParse(it) }, RainbowTransformation.Parser())
  }

  init {
    this.canParse = canParse
    this.parser = parser
  }
}
