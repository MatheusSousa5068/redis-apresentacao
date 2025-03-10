package br.edu.ifpb.dist.redis;

import java.util.List;
import java.util.Scanner;

import br.edu.ifpb.dist.redis.model.TodoItem;
import br.edu.ifpb.dist.redis.service.TodoService;

public class Main {
    public static void main(String[] args) {
        TodoService todoService = new TodoService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=======================================");
            System.out.println("          TO-DO APP - MENU");
            System.out.println("=======================================");
            System.out.println("1. Adicionar Tarefa");
            System.out.println("2. Listar Tarefas");
            System.out.println("3. Marcar Tarefa como Concluída");
            System.out.println("4. Excluir Tarefa");
            System.out.println("5. Sair");
            System.out.println("=======================================");
            System.out.print("Escolha uma opção: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\n--- Adicionar Tarefa ---");
                    System.out.print("Descrição da Tarefa: ");
                    String description = scanner.nextLine();
                    TodoItem todo = new TodoItem(null, description, false);
                    System.out.println(todoService.addTodo(todo));
                    break;

                case 2:
                    List<TodoItem> todos = todoService.getAllTodos();
                    System.out.println("\n--- Tarefas Cadastradas ---");
                    if (todos.isEmpty()) {
                        System.out.println("Nenhuma tarefa encontrada.");
                    } else {
                        for (TodoItem item : todos) {
                            String status = item.isCompleted() ? "Concluída" : "Pendente";
                            System.out.println("ID: " + item.getId() + " | Descrição: " + item.getDescription() + " | Status: " + status);
                        }
                    }
                    break;

                case 3:
                    System.out.println("\n--- Marcar Tarefa como Concluída ---");
                    System.out.print("ID da Tarefa a ser concluída: ");
                    String completeId = scanner.nextLine();
                    System.out.println(todoService.completeTodo(completeId));
                    break;

                case 4:
                    System.out.println("\n--- Excluir Tarefa ---");
                    System.out.print("ID da Tarefa a ser excluída: ");
                    String deleteId = scanner.nextLine();
                    System.out.println(todoService.deleteTodo(deleteId));
                    break;

                case 5:
                    System.out.println("\nSaindo do aplicativo... Até logo!");
                    todoService.close();
                    scanner.close();
                    return;

                default:
                    System.out.println("\nOpção inválida, por favor tente novamente.");
            }
        }
    }
}
