package ma.projet.graph.dtos;

import lombok.Data;
import ma.projet.graph.entities.TypeTransaction;

import java.time.LocalDate;

@Data
public class TransactionRequest {
    private Long compteId;     // ID du compte associé à la transaction
    private Double montant;    // Montant de la transaction
    private LocalDate date;       // Date de la transaction au format ISO (ex. : "2024-11-26")
    private TypeTransaction type;       // Type de la transaction : DEPOT ou RETRAIT

    public Long getCompteId() {
        return compteId;
    }

    public void setCompteId(Long compteId) {
        this.compteId = compteId;
    }

    public Double getMontant() {
        return montant;
    }
    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public TypeTransaction getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }
}
