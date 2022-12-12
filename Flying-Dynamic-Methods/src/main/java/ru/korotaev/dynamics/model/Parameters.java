package ru.korotaev.dynamics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Parameters {

    private double n;
    private double t;
    private double currentWeight;
    private double push;
    private double velocity;
    private double mach;
    private double cxa;
    private double alpha;
    private double fettaC;
    private double cya;
    private double dVdt;
    private double wz;
    private double tetta;
    private double y;
    private double dYdt;
    private double x;
    private double dXdt;
}
