
package com.yeu.code.jplag.enums;

import de.jplag.Language;
import de.jplag.c.CLanguage;
import de.jplag.cpp.CPPLanguage;
import de.jplag.java.JavaLanguage;
import de.jplag.javascript.JavaScriptLanguage;
import de.jplag.kotlin.KotlinLanguage;
import de.jplag.python3.PythonLanguage;
import de.jplag.typescript.TypeScriptLanguage;

public enum JPlagLanguage {
    Java("Java", new JavaLanguage()),
    TypeScript("TypeScript", new TypeScriptLanguage()),
    JavaScript("JavaScript", new JavaScriptLanguage()),
    C("C", new CLanguage()),
    Cpp("Cpp", new CPPLanguage()),
    Python("Python", new PythonLanguage()),
    Kotlin("Kotlin", new KotlinLanguage()),
    ;

    private final String name;
    private final Language language;

    public String getName() {
        return this.name;
    }

    public Language getLanguage() {
        return this.language;
    }

    private JPlagLanguage(String name, Language language) {
        this.name = name;
        this.language = language;
    }

    public static JPlagLanguage getByName(String name) {
        JPlagLanguage[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            JPlagLanguage language = var1[var3];
            if (language.getName().equals(name)) {
                return language;
            }
        }

        throw new IllegalArgumentException("No enum constant with name: " + name);
    }
}
