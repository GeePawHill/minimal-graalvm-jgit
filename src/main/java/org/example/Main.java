package org.example;

import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        try (Repository localRepo = new RepositoryBuilder()
                .findGitDir(Path.of(".").toFile()).build()) {
            Config configuration = localRepo.getConfig();
            String remote = configuration.getString("remote", "origin", "url");
            Path exercise = localRepo.getWorkTree().toPath();
            Ref head = localRepo.getAllRefs().get("HEAD");
            var hash = head.getObjectId().getName();
            System.out.println("Whatever");
        } catch (Exception cause) {
            throw new RuntimeException(cause);
        }
    }
}