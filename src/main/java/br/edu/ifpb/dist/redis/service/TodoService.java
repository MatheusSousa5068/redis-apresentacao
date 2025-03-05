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

    public void addTodo(TodoItem todo) {
        jedis.set(todo.getId(), todo.getDescription() + ":" + todo.isCompleted());
    }

    public List<TodoItem> getAllTodos() {
        List<TodoItem> todos = new ArrayList<>();
        for (String key : jedis.keys("*")) {
            String value = jedis.get(key);
            String[] parts = value.split(":");
            boolean completed = Boolean.parseBoolean(parts[1]);
            todos.add(new TodoItem(key, parts[0], completed));
        }
        return todos;
    }

    public void completeTodo(String id) {
        String value = jedis.get(id);
        if (value != null) {
            String description = value.split(":")[0];
            jedis.set(id, description + ":true");
        }
    }

    public void deleteTodo(String id) {
        jedis.del(id);
    }

    public void close() {
        jedis.close();
    }
}
