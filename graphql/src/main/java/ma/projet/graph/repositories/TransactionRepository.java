package ma.projet.graph.repositories;

import ma.projet.graph.entities.Compte;
import ma.projet.graph.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCompte(Compte compte); // Trouver les transactions d'un compte

    // Somme des dépôts (transactions de type DEPOT)
    @Query("SELECT SUM(t.montant) FROM Transaction t WHERE t.type = 'DEPOT'")
    Double sumDeposits();

    // Somme des retraits (transactions de type RETRAIT)
    @Query("SELECT SUM(t.montant) FROM Transaction t WHERE t.type = 'RETRAIT'")
    Double sumWithdrawals();

}
