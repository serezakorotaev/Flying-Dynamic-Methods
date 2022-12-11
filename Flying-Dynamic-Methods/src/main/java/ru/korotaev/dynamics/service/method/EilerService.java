package ru.korotaev.dynamics.service.method;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.korotaev.dynamics.service.DynamicService;

import static ru.korotaev.dynamics.constant.Constant.t0;

@Service
@RequiredArgsConstructor
public class EilerService {

    private final DynamicService dynamicService;

    public void eilerMethod(double dt, double alpha) {
        double t = t0;
        double vOld;
        double vNew;
        double fettaCOld;
        double fettaCNew;
        double tettaOld;
        double tettaNew;
        double xOld;
        double xNew;
        double yOld;
        double yNew;
        double wzOld;
        double wzNew;

        System.out.println("Вывод начальных данных, создание эксель");
        //

    }
}
