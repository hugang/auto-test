package io.hugang.execute.ext;

import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * command for git history
 * <p>
 * <br>target: target src path, which contains git repository
 * <br>value: result path
 */
public class GitHistoryCommand extends Command {
    private static final Log log = Log.get();

    public GitHistoryCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "gitHistory";
    }

    @Override
    public boolean _execute() {
        String targetPath = render(this.getTarget());
        String resultPath = getFilePath(render(this.getDictStr("value", this.getValue())));

        getGitHistory(targetPath, resultPath);
        this.appendDict("resultPath", resultPath);
        return true;
    }

    private void getGitHistory(String targetPath, String resultPath) {
        FileWriter fileWriter = new FileWriter(resultPath);

        List<GitHistory> gitHistories = getCommitLogsWithFileList(targetPath);
        if (ObjectUtil.isEmpty(gitHistories)) {
            log.error("get git history failed");
            return;
        }

        fileWriter.write("commit,author,date,message,type,path\n");
        for (GitHistory gitHistory : gitHistories) {
            fileWriter.append(gitHistory.getCommit() + "," + gitHistory.getAuthor() + "," + gitHistory.getDate() + "," + gitHistory.getMessage() + "," + gitHistory.getType() + "," + gitHistory.getPath() + "\n");
        }

    }

    /**
     * get commit logs with file list
     *
     * @param targetPath target path
     * @return commit logs
     */
    private List<GitHistory> getCommitLogsWithFileList(String targetPath) {
        List<GitHistory> gitHistories = new ArrayList<>();
        try {
            Repository repository = new FileRepositoryBuilder().setGitDir(new File(targetPath + "/.git")).setMustExist(true).build();

            Git git = new Git(repository);
            Iterable<RevCommit> logs = git.log().call();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            RevCommit prevCommit = null;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (DiffFormatter format = new DiffFormatter(outputStream)) {
                format.setRepository(repository);
                for (RevCommit commit : logs) {
                    if (prevCommit != null) {
                        AbstractTreeIterator newTree = getCanonicalTreeParser(prevCommit, repository);
                        AbstractTreeIterator oldTree = getCanonicalTreeParser(commit, repository);
                        List<DiffEntry> entries = format.scan(oldTree, newTree);
                        for (DiffEntry entry : entries) {
                            GitHistory gitHistory = new GitHistory();
                            gitHistory.setCommit(prevCommit.getName());
                            gitHistory.setAuthor(prevCommit.getAuthorIdent().getName());
                            gitHistory.setDate(sdf.format(prevCommit.getAuthorIdent().getWhen()));
                            gitHistory.setMessage(prevCommit.getShortMessage());
                            gitHistory.setType(entry.getChangeType().name());
                            gitHistory.setOldPath(entry.getOldPath());
                            gitHistory.setNewPath(entry.getNewPath());
                            gitHistories.add(gitHistory);
                        }
                    }
                    prevCommit = commit;
                }
                assert prevCommit != null;
                // initial commit
                List<DiffEntry> initialEntries = format.scan(new EmptyTreeIterator(), getCanonicalTreeParser(prevCommit, repository));
                for (DiffEntry entry : initialEntries) {
                    GitHistory gitHistory = new GitHistory();
                    gitHistory.setCommit(prevCommit.getName());
                    gitHistory.setAuthor(prevCommit.getAuthorIdent().getName());
                    gitHistory.setDate(sdf.format(prevCommit.getAuthorIdent().getWhen()));
                    gitHistory.setMessage(prevCommit.getShortMessage());
                    gitHistory.setType(entry.getChangeType().name());
                    gitHistory.setOldPath(entry.getOldPath());
                    gitHistory.setNewPath(entry.getNewPath());
                    gitHistories.add(gitHistory);
                }
            }
            git.close();
        } catch (IOException | GitAPIException e) {
            throw new RuntimeException(e);
        }
        return gitHistories;
    }

    private AbstractTreeIterator getCanonicalTreeParser(RevCommit prevCommit, Repository repository) throws IOException {
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(prevCommit);
            ObjectId treeId = commit.getTree().getId();
            try (ObjectReader reader = repository.newObjectReader()) {
                return new CanonicalTreeParser(null, reader, treeId);
            }
        }
    }

    public static class GitHistory {
        private String commit;
        private String author;
        private String date;
        private String message;
        private String type;
        private String oldPath;
        private String newPath;

        public String getCommit() {
            return commit;
        }

        public void setCommit(String commit) {
            this.commit = commit;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPath() {
            if ("ADD".equals(type) || "COPY".equals(type) || "RENAME".equals(type)) {
                return newPath;
            } else {
                return oldPath;
            }
        }

        public String getOldPath() {
            return oldPath;
        }

        public void setOldPath(String oldPath) {
            this.oldPath = oldPath;
        }

        public String getNewPath() {
            return newPath;
        }

        public void setNewPath(String newPath) {
            this.newPath = newPath;
        }
    }
}
