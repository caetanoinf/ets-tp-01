import net.jqwik.api.*;
import net.jqwik.api.constraints.Positive;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class CalculoIMCTest {
    @Nested
    @DisplayName("Grupo de testes válidos")
    class Validos {
        @Test
        void testa_calculo_IMC() {
            assertThat(CalculoIMC.calcularPeso(50, 1.70)).isEqualTo(17.301038062283737);
            assertThat(CalculoIMC.calcularPeso(70, 1.70)).isEqualTo(24.221453287197235);
            assertThat(CalculoIMC.calcularPeso(80, 1.70)).isEqualTo(27.68166089965398);
            assertThat(CalculoIMC.calcularPeso(90, 1.60)).isEqualTo(35.15624999999999);
        }

        @Test
        void testa_calculo_classificacao() {
            assertThat(CalculoIMC.classificarIMC(15.0)).isEqualTo("Magreza grave");
            assertThat(CalculoIMC.classificarIMC(16.0)).isEqualTo("Magreza moderada");
            assertThat(CalculoIMC.classificarIMC(17.0)).isEqualTo("Magreza leve");
            assertThat(CalculoIMC.classificarIMC(18.5)).isEqualTo("Saudável");
            assertThat(CalculoIMC.classificarIMC(25.0)).isEqualTo("Sobrepeso");
            assertThat(CalculoIMC.classificarIMC(30.0)).isEqualTo("Obesidade Grau I");
            assertThat(CalculoIMC.classificarIMC(35.0)).isEqualTo("Obesidade Grau II");
            assertThat(CalculoIMC.classificarIMC(40.0)).isEqualTo("Obesidade Grau III");
        }
    }

    @Nested
    @DisplayName("Grupo de testes inválidos")
    class Invalidos {
        @Test()
        void testa_calculo_IMC_com_valores_negativos() {
            assertThatThrownBy(() -> CalculoIMC.calcularPeso(-10, 1.80)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> CalculoIMC.calcularPeso(60, -1.70)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Grupo de testes extremos")
    class Extremos {
        @Test()
        void testa_calculo_IMC_com_valores_muito_grandes() {
            assertThatThrownBy(() -> CalculoIMC.calcularPeso(1000, 1.80)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> CalculoIMC.calcularPeso(1000, 1000.0)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test()
        void testa_calculo_IMC_com_valores_muito_baixos() {
            assertThatThrownBy(() -> CalculoIMC.calcularPeso(0, 1.80)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> CalculoIMC.calcularPeso(0, 0.0)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Property
    void testa_calculo_IMC_sempre_positivo(@ForAll @Positive double peso, @ForAll @Positive double altura) {
        assertThat(CalculoIMC.calcularPeso(peso, altura)).isGreaterThanOrEqualTo(0);
    }

    @Property
    void teste_calculo_IMC_valores_aleatorios(@ForAll double peso, @ForAll double altura) {
        assertThat(CalculoIMC.calcularPeso(peso, altura)).isGreaterThanOrEqualTo(0);
    }

    @Property
    void test_calculo_IMC_valores_especificos(@ForAll("pesosExtremos") double peso, @ForAll("alturasExtremas") double altura) {
        assertThat(CalculoIMC.calcularPeso(peso, altura)).isGreaterThanOrEqualTo(0);
    }

    @Provide
    Arbitrary<Double> alturasExtremas() {
        return Arbitraries.of(0.5, 3.0, 5.0, 0.0, 1000.0);
    }

    @Provide
    Arbitrary<Double> pesosExtremos() {
        return Arbitraries.of(1.0, 500.0, 0.0, -1000.0, 1000000.0);
    }

    @Test
    void testa_calculo_IMC_com_mockito() {
        var scopedMock = mockStatic(CalculoIMC.class);
        when(CalculoIMC.calcularPeso(anyDouble(), anyDouble())).thenReturn(34.0);
        assertThat(CalculoIMC.calcularPeso(50, 1.70)).isEqualTo(34.0);
        scopedMock.close();
    }

    @Example
    void testa_calculo_IMC_com_example() {
        assertThat(CalculoIMC.calcularPeso(50, 1.70)).isEqualTo(17.301038062283737);
    }
}
