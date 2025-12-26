package ui.ft.ccit.faculty.transaksi.barang.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import ui.ft.ccit.faculty.transaksi.barang.model.Barang;
import ui.ft.ccit.faculty.transaksi.barang.view.BarangService;

@RestController
@RequestMapping("/api/barang")
@Tag(name = "Barang API", description = "CRUD dan pencarian data barang")
public class BarangController {

    private final BarangService service;

    public BarangController(BarangService service) {
        this.service = service;
    }

    // ============================
    // GET ALL (optional pagination)
    // ============================
    @GetMapping
    @Operation(
        summary = "Mengambil daftar semua barang",
        description = "Mengambil seluruh data barang. Pagination bersifat opsional."
    )
    @ApiResponse(responseCode = "200", description = "Berhasil mengambil data")
    public List<Barang> list(
            @Parameter(description = "Nomor halaman (dimulai dari 0)")
            @RequestParam(required = false) Integer page,

            @Parameter(description = "Jumlah data per halaman")
            @RequestParam(required = false) Integer size) {

        // TANPA pagination
        if (page == null && size == null) {
            return service.getAll();
        }

        // DENGAN pagination
        int p = (page != null && page >= 0) ? page : 0;
        int s = (size != null && size > 0) ? size : 5;
        return service.getAllWithPagination(p, s);
    }

    // ============================
    // GET BY ID
    // ============================
    @GetMapping("/{id}")
    @Operation(
        summary = "Mengambil detail satu barang",
        description = "Mengambil detail barang berdasarkan ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Data ditemukan"),
        @ApiResponse(responseCode = "404", description = "Data tidak ditemukan")
    })
    public Barang get(
            @Parameter(description = "ID barang", required = true)
            @PathVariable String id) {
        return service.getById(id);
    }

    // ============================
    // SEARCH BY NAMA
    // ============================
    @GetMapping("/search")
    @Operation(
        summary = "Mencari barang berdasarkan nama",
        description = "Mencari barang menggunakan keyword pada nama."
    )
    public List<Barang> search(
            @Parameter(description = "Kata kunci pencarian", required = true)
            @RequestParam String q) {
        return service.searchByNama(q);
    }

    // ============================
    // CREATE BARANG
    // ============================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Membuat barang baru",
        description = "Menambahkan satu data barang ke dalam sistem."
    )
    @ApiResponse(responseCode = "201", description = "Barang berhasil dibuat")
    public Barang create(
            @RequestBody Barang barang) {
        return service.save(barang);
    }

    // ============================
    // CREATE BARANG BULK
    // ============================
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Membuat barang secara bulk",
        description = "Menambahkan banyak data barang dalam satu request."
    )
    public List<Barang> createBulk(
            @RequestBody List<Barang> barang) {
        return service.saveBulk(barang);
    }

    // ============================
    // UPDATE BARANG
    // ============================
    @PutMapping("/{id}")
    @Operation(
        summary = "Memperbarui data barang",
        description = "Memperbarui data barang berdasarkan ID."
    )
    public Barang update(
            @Parameter(description = "ID barang", required = true)
            @PathVariable String id,
            @RequestBody Barang barang) {
        return service.update(id, barang);
    }

    // ============================
    // DELETE BULK
    // ============================
    @DeleteMapping("/bulk")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Menghapus barang secara bulk",
        description = "Menghapus banyak barang berdasarkan daftar ID."
    )
    public void deleteBulk(
            @RequestBody List<String> ids) {
        service.deleteBulk(ids);
    }

    // ============================
    // DELETE BY ID
    // ============================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Menghapus satu barang",
        description = "Menghapus satu data barang berdasarkan ID."
    )
    public void delete(
            @Parameter(description = "ID barang", required = true)
            @PathVariable String id) {
        service.delete(id);
    }
}
