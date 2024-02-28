package com.media.recommendations.model;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "First is mandatory")
    @Column(name = "first", nullable = false)
    private long first;

    @NotNull(message = "Second is mandatory")
    @Column(name = "second", nullable = false)
    private long second;

    @NotNull(message = "Rating is mandatory")
    @Column(name = "rating", nullable = false)
    private boolean rating;

    @NotNull(message = "Date is mandatory")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotBlank(message = "First type is mandatory")
    @Column(name = "first_type", nullable = false)
    private String firstType;

    @NotBlank(message = "Second type is mandatory")
    @Column(name = "second_type", nullable = false)
    private String secondType;
}
