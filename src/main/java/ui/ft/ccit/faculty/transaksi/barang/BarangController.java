package ui.ft.ccit.faculty.transaksi.barang;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barang")
public class BarangController {

    private final BarangService service;

    public BarangController(BarangService service) {
        this.service = service;
    }

    @GetMapping
    public List<Barang> list() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Barang get(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping("/search")
    public List<Barang> search(@RequestParam String q) {
        return service.searchByNama(q);
    }

    @PostMapping
    public Barang create(@RequestBody Barang barang) {
        return service.save(barang);
    }
}
