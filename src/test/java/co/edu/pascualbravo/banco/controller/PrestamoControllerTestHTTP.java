package co.edu.pascualbravo.banco.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PrestamoControllerTestHTTP {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void solicitarPrestamoValidoDevuelve200YEstadoAprobado() throws Exception {
        mockMvc.perform(post("/api/prestamos/solicitar")
                        .param("clienteId", "1")
                        .param("monto", "20000")
                        .param("tipoCliente", "PREMIUM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("APROBADO"))
                .andExpect(jsonPath("$.tasaInteres").value(3.5));
    }

    @Test
    void solicitarPrestamoSobreLimiteDevuelveRechazado() throws Exception {
        mockMvc.perform(post("/api/prestamos/solicitar")
                        .param("clienteId", "1")
                        .param("monto", "60000")
                        .param("tipoCliente", "REGULAR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("RECHAZADO"));
    }
}