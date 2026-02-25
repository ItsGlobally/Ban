package top.itsglobally.ban.util;

import org.bukkit.Bukkit;
import top.itsglobally.ban.data.Global;

public class Async implements Global {



    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static void runSync(Runnable runnable) {
        Bukkit.getScheduler().runTask(plugin, runnable);
    }

    public static <T> void supply(java.util.concurrent.Callable<T> callable, java.util.function.Consumer<T> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            T result = null;
            try {
                result = callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            T finalResult = result;
            Bukkit.getScheduler().runTask(plugin, () -> callback.accept(finalResult));
        });
    }
}