package com.JGit;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**JGit API测试*/
public class gitTest {

    private String remotePath = "http://172.17.1.247:18080/root/SpringCloudConfig.git";//远程库路径
    private String username = "root";
    private String password = "12345678";
    private String localPath = "D:\\project\\";//下载已有仓库到本地路径
    private String tmpPath = "D:\\tmp\\";//临时路径
    private String initPath = "D:\\test\\";//本地路径新建

    /**克隆远程库*/
    @Test
    public void testClone() {
        try{
            //设置远程服务器上的用户名和密码
            UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new
                    UsernamePasswordCredentialsProvider(username,password);
            //克隆代码库命令
            CloneCommand cloneCommand = Git.cloneRepository();
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
            String suffix = sdf.format(new Date());
            tmpPath = tmpPath + "\\" +suffix;
            Git git= cloneCommand.setURI(remotePath) //设置远程URI
                    .setBranch("master") //设置clone下来的分支
                    .setDirectory(new File(tmpPath)) //设置下载存放路径
                    .setCredentialsProvider(usernamePasswordCredentialsProvider) //设置权限验证
                    .call();
            System.out.print(git.tag());
            /*如果localPath存在则删除该路径*/
            FileUtils.deleteDirectory(new File(localPath));
            FileUtils.copyDirectory(new File(tmpPath),new File(localPath));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteFile(File file){
        if (file.exists()){
            if (file.isFile()){
                file.delete();
            }else if (file.isDirectory()){
                File[] files = file.listFiles();
                for (File f : files){
                    deleteFile(f);
                }
                /*delete()只能删除空文件夹；执行到这里说明files为空；*/
                file.delete();
            }
        }
    }
    /**本地新建仓库*/
    @Test
    public void testCreate() throws IOException {
        //本地新建仓库地址
        Repository newRepo = FileRepositoryBuilder.create(new File(initPath + "/.git"));
        newRepo.create();
    }

    /**本地仓库新增文件*/
    @Test
    public void testAdd() throws IOException, GitAPIException {
        File myfile = new File(localPath + "/myfile.txt");
        myfile.createNewFile();
        //git仓库地址
        Git git = new Git(new FileRepository(localPath+"/.git"));
        //添加文件
        git.add().addFilepattern("myfile").call();
    }

    /**本地提交代码*/
    @Test
    public void testCommit() throws IOException, GitAPIException,
            JGitInternalException {
        //git仓库地址
        Git git = new Git(new FileRepository(localPath+"/.git"));
        //提交代码
        git.commit().setMessage("test jGit").call();
    }


    /**拉取远程仓库内容到本地*/
    @Test
    public void testPull() throws IOException, GitAPIException {
        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new
                UsernamePasswordCredentialsProvider(username,password);
        //git仓库地址
        Git git = new Git(new FileRepository(remotePath+"/.git"));
        git.pull().setRemoteBranchName("master").
                setCredentialsProvider(usernamePasswordCredentialsProvider).call();
    }

    /**push本地代码到远程仓库地址*/
    @Test
    public void testPush() throws IOException, JGitInternalException,
            GitAPIException {
        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new
                UsernamePasswordCredentialsProvider(username,password);
        //git仓库地址
        Git git = new Git(new FileRepository(localPath+"/.git"));   
        git.push().setRemote("origin").setCredentialsProvider(usernamePasswordCredentialsProvider).call();
    }
}