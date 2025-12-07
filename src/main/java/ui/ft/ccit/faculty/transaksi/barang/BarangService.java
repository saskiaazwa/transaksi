package ui.ft.ccit.faculty.transaksi.barang;

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

    public List<Barang> getAll() {
        return barangRepository.findAll();
    }

    public Barang getById(String id) {
        return barangRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Barang tidak ditemukan: " + id));
    }

    public List<Barang> searchByNama(String keyword) {
        return barangRepository.findByNamaContainingIgnoreCase(keyword);
    }

    public Barang save(Barang barang) {
        return barangRepository.save(barang);
    }
}
