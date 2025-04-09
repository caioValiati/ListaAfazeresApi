package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lista_tarefa")
public class ListaTarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "listaTarefa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarefa> tasks = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Construtores
    public ListaTarefa() {
    }

    public ListaTarefa(String title) {
        this.title = title;
    }

    // Métodos para gerenciar a relação bidirecional
    public void addTarefa(Tarefa tarefa) {
        tasks.add(tarefa);
        tarefa.setListaTarefa(this);
    }

    public void removeTarefa(Tarefa tarefa) {
        tasks.remove(tarefa);
        tarefa.setListaTarefa(null);
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<Tarefa> getTasks() {
        return tasks;
    }

    public void setTasks(List<Tarefa> tasks) {
        this.tasks = tasks;
    }
}