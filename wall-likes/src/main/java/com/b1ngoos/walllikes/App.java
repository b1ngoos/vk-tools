package com.b1ngoos.walllikes;

import com.b1ngoos.walllikes.domain.UserLike;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

public class App
{

    public static void main( String[] args ) throws IOException, InterruptedException {

        writeWelcome();
        int id = getInput();
        writeFilter();

        int min = getInput();

        writeMessage();

        VkTools vk = new VkToolsImpl();

//        FriendsCollection friends = vk.getFriends(ID);
//
//        for(int id: friends.getIds()) {
//            vk.getWallPosts(ID);
//        }

        Set<UserLike> userLikes = vk.getWallPosts(id);

        for (UserLike user: userLikes) {
            if(user.getLikes() >= min)
            System.out.println(vk.getNameById(user.getId()) + " : " + user.getLikes());
            Thread.sleep(50);
        }

    }

    private static int getInput() {
        Scanner scan = new Scanner(System.in);

        while(!scan.hasNextInt()) {
            scan.next();
        }
        int input = scan.nextInt();

        return input;
    }

    private static void writeMessage() {
        System.out.println("\n============================== ");
        System.out.println("Let's calculate");
        System.out.println("============================== ");
    }

    private static void writeWelcome() {
        System.out.println("\t\tWall likes ");
        System.out.println("============================== ");
        System.out.print("Input # ID: ");
    }

    private static void writeFilter() {
        System.out.println("============================== ");
        System.out.print("Input min count of likes by one user (1 and more) : ");
    }
}
