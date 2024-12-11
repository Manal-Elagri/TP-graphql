package ma.projet.graph.controllers;

import lombok.AllArgsConstructor;
import ma.projet.graph.dtos.TransactionRequest;
import ma.projet.graph.entities.Compte;
import ma.projet.graph.entities.Transaction;
import ma.projet.graph.entities.TypeCompte;
import ma.projet.graph.repositories.CompteRepository;
import ma.projet.graph.repositories.TransactionRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class CompteControllerGraphQL {

    private CompteRepository compteRepository;
    private  TransactionRepository transactionRepository;

    // Requête: Transactions associées à un compte (par ID)
    @QueryMapping
    public List<Transaction> compteTransactions(@Argument Long id) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte not found with ID: " + id));
        return transactionRepository.findByCompte(compte); // Vérifiez si cette méthode existe.
    }

    // Requête: Toutes les transactions
    @QueryMapping
    public List<Transaction> allTransactions() {
        return transactionRepository.findAll();
    }

    @MutationMapping
    public Transaction addTransaction(@Argument TransactionRequest transactionRequest) {
        // Trouver le compte associé à l'ID donné
        Compte compte = compteRepository.findById(transactionRequest.getCompteId())
                .orElseThrow(() -> new RuntimeException("Compte not found"));

        // Convertir LocalDate en java.util.Date
        Date transactionDate = java.sql.Date.valueOf(transactionRequest.getDate()); // conversion de LocalDate à Date

        // Créer une nouvelle transaction
        Transaction transaction = new Transaction();
        transaction.setMontant(transactionRequest.getMontant());
        transaction.setDate(transactionDate);  // utiliser la date convertie
        transaction.setType(transactionRequest.getType());
        transaction.setCompte(compte);

        // Sauvegarder la transaction
        transactionRepository.save(transaction);

        return transaction;
    }

    // Requête: Statistiques globales sur les transactions
    @QueryMapping
    public Map<String, Object> transactionStats() {
        long totalCount = transactionRepository.count();
        double totalDeposits = transactionRepository.sumDeposits() != null ? transactionRepository.sumDeposits() : 0;
        double totalWithdrawals = transactionRepository.sumWithdrawals() != null ? transactionRepository.sumWithdrawals() : 0;

        return Map.of(
                "totalCount", totalCount,
                "totalDepots", totalDeposits,
                "totalRetraits", totalWithdrawals
        );
    }





    @QueryMapping
    public List<Compte> allComptes(){
        return compteRepository.findAll();
    }

    @QueryMapping
    public Compte compteById(@Argument Long id){
        Compte compte =  compteRepository.findById(id).orElse(null);
        if(compte == null) throw new RuntimeException(String.format("Compte %s not found", id));
        else return compte;
    }


    @QueryMapping
    public List<Compte> compteByType(@Argument TypeCompte type) {
        return compteRepository.findByType(type);
    }

    @MutationMapping
    public Compte saveCompte(@Argument Compte compte){
       return compteRepository.save(compte);
    }

    @QueryMapping
    public Map<String, Object> totalSolde() {
        long count = compteRepository.count(); // Nombre total de comptes
        double sum = compteRepository.sumSoldes(); // Somme totale des soldes
        double average = count > 0 ? sum / count : 0; // Moyenne des soldes

        return Map.of(
                "count", count,
                "sum", sum,
                "average", average
        );
    }

    @MutationMapping
    public boolean deleteCompte(@Argument Long id) {
        if (compteRepository.existsById(id)) {
            compteRepository.deleteById(id);
            return true;
        } else {
            throw new RuntimeException(String.format("Compte with ID %s not found", id));
        }
    }


}
