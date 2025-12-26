package ui.ft.ccit.faculty.transaksi.barang.view;

import ui.ft.ccit.faculty.transaksi.DataAlreadyExistsException;
import ui.ft.ccit.faculty.transaksi.DataNotFoundException;
import ui.ft.ccit.faculty.transaksi.barang.model.Barang;
import ui.ft.ccit.faculty.transaksi.barang.model.BarangRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BarangService {

    private final BarangRepository barangRepository;

    public BarangService(BarangRepository barangRepository) {
        this.barangRepository = barangRepository;
    }

    // ============================
    // GET ALL
    // ============================
    public List<Barang> getAll() {
        return barangRepository.findAll();
    }

    public List<Barang> getAllWithPagination(int page, int size) {
        return barangRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    // ============================
    // GET BY ID
    // ============================
    public Barang getById(String idBarang) {
        return barangRepository.findById(idBarang)
                .orElseThrow(() -> new DataNotFoundException("Barang", idBarang));
    }

    // ============================
    // SEARCH
    // ============================
    public List<Barang> searchByNama(String keyword) {
        return barangRepository.findByNamaContainingIgnoreCase(keyword);
    }

    // ============================
    // CREATE
    // ============================
    public Barang save(Barang barang) {

        if (barang.getIdBarang() == null || barang.getIdBarang().isBlank()) {
            throw new IllegalArgumentException("idBarang wajib diisi");
        }

        if (barangRepository.existsById(barang.getIdBarang())) {
            throw new DataAlreadyExistsException("Barang", barang.getIdBarang());
        }

        return barangRepository.save(barang);
    }

    // ============================
    // CREATE BULK
    // ============================
    public List<Barang> saveBulk(List<Barang> barangList) {

        if (barangList == null || barangList.isEmpty()) {
            throw new IllegalArgumentException("Data barang tidak boleh kosong");
        }

        for (Barang barang : barangList) {

            if (barang.getIdBarang() == null || barang.getIdBarang().isBlank()) {
                throw new IllegalArgumentException("idBarang wajib diisi untuk setiap barang");
            }

            if (barangRepository.existsById(barang.getIdBarang())) {
                throw new DataAlreadyExistsException("Barang", barang.getIdBarang());
            }
        }

        return barangRepository.saveAll(barangList);
    }

    // ============================
    // UPDATE
    // ============================
    public Barang update(String idBarang, Barang updated) {

        Barang existing = getById(idBarang); // otomatis validasi existence

        existing.setNama(updated.getNama());
        existing.setStok(updated.getStok());
        existing.setHarga(updated.getHarga());
        existing.setPersenLaba(updated.getPersenLaba());
        existing.setDiskon(updated.getDiskon());
        existing.setIdJenisBarang(updated.getIdJenisBarang());
        existing.setIdPemasok(updated.getIdPemasok());

        return barangRepository.save(existing);
    }

    // ============================
    // DELETE BULK
    // ============================
    public void deleteBulk(List<String> ids) {

        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("List ID tidak boleh kosong");
        }

        // safety limit
        if (ids.size() > 100) {
            throw new IllegalArgumentException("Maksimal 100 data per bulk delete");
        }

        // validasi semua ID ada
        long existingCount = barangRepository.countByIdBarangIn(ids);
        if (existingCount != ids.size()) {
            throw new IllegalStateException(
                "Sebagian ID tidak ditemukan, operasi bulk delete dibatalkan"
            );
        }

        barangRepository.deleteAllById(ids);
    }

    // ============================
    // DELETE BY ID
    // ============================
    public void delete(String idBarang) {

        if (!barangRepository.existsById(idBarang)) {
            throw new DataNotFoundException("Barang", idBarang);
        }

        barangRepository.deleteById(idBarang);
    }
}
