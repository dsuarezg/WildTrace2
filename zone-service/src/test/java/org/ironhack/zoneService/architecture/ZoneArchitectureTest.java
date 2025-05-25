package org.ironhack.zoneService.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.Architectures.LayeredArchitecture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ZoneArchitectureTest {

    @Test
    @DisplayName("Should verify a correct layered architecture")
    void verifyArchitecture() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("org.ironhack.zoneService");

        LayeredArchitecture architecture = Architectures.layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controller").definedBy("..controller..")
                .layer("Service").definedBy("..service..")
                .layer("Repository").definedBy("..repository..")
                .layer("Model").definedBy("..model..")
                .layer("DTO").definedBy("..dto..")
                .layer("Exception").definedBy("..exception..")
                .layer("Mapper").definedBy("..mapper..")

                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
                .whereLayer("DTO").mayOnlyBeAccessedByLayers("Controller", "Service", "Mapper")
                .whereLayer("Model").mayOnlyBeAccessedByLayers("Repository", "Service", "Mapper")
                .whereLayer("Mapper").mayOnlyBeAccessedByLayers("Service");

        architecture.check(classes);
    }
}