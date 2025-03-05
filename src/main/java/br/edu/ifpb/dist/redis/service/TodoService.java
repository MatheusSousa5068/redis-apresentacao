package br.edu.ifpb.dist.redis.service;

import redis.clients.jedis.Jedis;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.dist.redis.model.TodoItem;

public class TodoService {
    private Jedis jedis;

    public TodoService() {
        this.jedis = new Jedis("localhost", 6379);
    }

    /**
     * Adiciona uma nova tarefa e gera automaticamente um ID no Redis.
     * @param todo Objeto TodoItem com a descrição da tarefa.
     * @return ID gerado pelo Redis ou uma mensagem de erro em caso de falha.
     */
    public String addTodo(TodoItem todo) {
        try {
            long id = jedis.incr("todo:id");
            String key = String.valueOf(id);
            jedis.set(key, todo.getDescription() + ":" + todo.isCompleted());
            return "Tarefa adicionada com ID: " + key;
        } catch (Exception e) {
            return "Erro ao adicionar tarefa: " + e.getMessage();
        }
    }

    /**
     * Obtém todas as tarefas cadastradas no Redis.
     * @return Lista de TodoItem ou uma mensagem de erro.
     */
    public List<TodoItem> getAllTodos() {
        List<TodoItem> todos = new ArrayList<>();
        try {
            for (String key : jedis.keys("*")) {
                if ("todo:id".equals(key)) {
                    continue;
                }
                String value = jedis.get(key);
                String[] parts = value.split(":");


                if (parts.length == 2) {
                    boolean completed = Boolean.parseBoolean(parts[1]);
                    todos.add(new TodoItem(key, parts[0], completed));
                } else {
                    throw new Exception("Erro de formato nos dados da tarefa (ID: " + key + ").");
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao obter todas as tarefas: " + e.getMessage());
        }
        return todos;
    }

    /**
     * Marca uma tarefa como concluída.
     * @param id ID da tarefa a ser concluída.
     * @return Mensagem indicando sucesso ou falha.
     */
    public String completeTodo(String id) {
        try {
            String value = jedis.get(id);
            if (value != null) {
                String[] parts = value.split(":");
                if (parts.length == 2) {
                    String description = parts[0];
                    jedis.set(id, description + ":true");
                    return "Tarefa " + id + " marcada como concluída!";
                } else {
                    return "Erro de formato na tarefa com ID: " + id;
                }
            } else {
                return "Tarefa não encontrada com o ID: " + id;
            }
        } catch (Exception e) {
            return "Erro ao marcar tarefa como concluída (ID: " + id + "): " + e.getMessage();
        }
    }

    /**
     * Exclui uma tarefa pelo ID.
     * @param id ID da tarefa a ser excluída.
     * @return Mensagem indicando sucesso ou falha.
     */
    public String deleteTodo(String id) {
        try {
            long result = jedis.del(id);
            if (result == 0) {
                return "Tarefa não encontrada com o ID: " + id;
            }
            return "Tarefa " + id + " excluída com sucesso!";
        } catch (Exception e) {
            return "Erro ao excluir tarefa (ID: " + id + "): " + e.getMessage();
        }
    }

    /**
     * Fecha a conexão com o Redis.
     */
    public void close() {
        if (jedis != null) {
            try {
                jedis.close();
            } catch (Exception e) {
                System.err.println("Erro ao fechar a conexão com o Redis: " + e.getMessage());
            }
        }
    }
}
