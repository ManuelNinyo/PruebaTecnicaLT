package manuel.pruebatecnica;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empresas/prueba")

public class PruebaController {



    @GetMapping
    public String listar() {
        return "asd";
    }

}