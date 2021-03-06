/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.descriptors

import org.jetbrains.kotlin.builtins.KotlinBuiltIns
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.platform.PlatformToKotlinClassMap
import org.jetbrains.kotlin.resolve.ImportPath
import org.jetbrains.kotlin.types.TypeSubstitutor

interface ModuleDescriptor : DeclarationDescriptor, ModuleParameters {
    override fun getContainingDeclaration(): DeclarationDescriptor? = null

    val builtIns: KotlinBuiltIns

    fun shouldSeeInternalsOf(targetModule: ModuleDescriptor): Boolean

    override fun substitute(substitutor: TypeSubstitutor): ModuleDescriptor {
        return this
    }

    override fun <R, D> accept(visitor: DeclarationDescriptorVisitor<R, D>, data: D): R {
        return visitor.visitModuleDeclaration(this, data)
    }

    fun getPackage(fqName: FqName): PackageViewDescriptor

    fun getSubPackagesOf(fqName: FqName, nameFilter: (Name) -> Boolean): Collection<FqName>

    fun <T> getCapability(capability: Capability<T>): T?

    class Capability<T>(val name: String)
}

interface ModuleParameters {
    val defaultImports: List<ImportPath>
    val excludedImports: List<FqName> get() = emptyList()
    val platformToKotlinClassMap: PlatformToKotlinClassMap

    object Empty: ModuleParameters {
        override val defaultImports: List<ImportPath> = emptyList()
        override val platformToKotlinClassMap: PlatformToKotlinClassMap = PlatformToKotlinClassMap.EMPTY
    }
}

fun ModuleParameters(defaultImports: List<ImportPath>, excludedImports: List<FqName>, platformToKotlinClassMap: PlatformToKotlinClassMap): ModuleParameters =
        object : ModuleParameters {
            override val defaultImports: List<ImportPath> = defaultImports
            override val excludedImports: List<FqName> get() = excludedImports
            override val platformToKotlinClassMap: PlatformToKotlinClassMap = platformToKotlinClassMap
        }
