package tobyspring.learning.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@AnalyzeClasses(packages = "tobyspring.learning.archunit")
public class ArchUnitLearningTest {

    @ArchTest
    void application(JavaClasses classes) {
        classes().that().resideInAPackage("..application..")
                .should().onlyHaveDependentClassesThat()
                .resideInAnyPackage("..application..", "..adapter..")
                .check(classes);

    }

    @ArchTest
    void adapter(JavaClasses classes) {
        noClasses().that().resideInAPackage("..application..")
                .should().dependOnClassesThat().resideInAPackage("..adapter..")
                .check(classes);
    }

    @ArchTest
    void domain(JavaClasses classes) {
        classes().that().resideInAPackage("..domain..")
                .should().onlyDependOnClassesThat().resideInAnyPackage("..domain..","..java..")
                .check(classes);

    }
}
