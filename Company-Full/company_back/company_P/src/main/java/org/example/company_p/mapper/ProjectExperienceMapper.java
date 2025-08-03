package org.example.company_p.mapper;

import org.example.company_p.dto.ProjectExperienceDto;
import org.example.company_p.entity.ProjectExperience;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.TextStyle;
import java.util.Locale;

@Mapper(componentModel = "spring")
public interface ProjectExperienceMapper {

    // Entity → DTO dönüşümü
    @Mapping(target = "durationText", source = ".", qualifiedByName = "calcDuration")
    @Mapping(target = "active", source = ".", qualifiedByName = "isProjectActive")
    @Mapping(target = "employeeName", source = "employeeName") // doğrudan taşı
    @Mapping(target = "employeeSurname", source = "employeeSurname") // doğrudan taşı
    ProjectExperienceDto toDto(ProjectExperience experience);

    // DTO → Entity dönüşümü
    ProjectExperience toEntity(ProjectExperienceDto dto);

    /**
     * Projenin aktif olup olmadığını hesaplar.
     * End date yoksa veya bugünden sonra ise aktif kabul edilir.
     */
    @Named("isProjectActive")
    static boolean isProjectActive(ProjectExperience e) {
        return e.getEndDate() == null || e.getEndDate().isAfter(LocalDate.now());
    }

    /**
     * Duration metnini hesaplar.
     * Artık `employeeStartDate` ve `employeeEndDate` yok; `startDate` ve `endDate` kullanılacak.
     */
    @Named("calcDuration")
    static String calcDuration(ProjectExperience e) {
        if (e == null || e.getStartDate() == null) return "";

        LocalDate start = e.getStartDate();
        LocalDate end = (e.getEndDate() != null) ? e.getEndDate() : LocalDate.now();
        Period p = Period.between(start, end);

        String startStr = start.getMonth().getDisplayName(TextStyle.SHORT, new Locale("tr")) + " " + start.getYear();
        String endStr = (e.getEndDate() == null)
                ? "Still"
                : end.getMonth().getDisplayName(TextStyle.SHORT, new Locale("tr")) + " " + end.getYear();

        String dur = (p.getYears() > 0 ? p.getYears() + " year " : "") +
                (p.getMonths() > 0 ? p.getMonths() + " month" : "");

        return startStr + " - " + endStr + "  " + dur.trim();
    }
}
