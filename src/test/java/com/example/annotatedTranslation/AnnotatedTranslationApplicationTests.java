package com.example.annotatedTranslation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.Deque;
import java.util.LinkedList;

class AnnotatedTranslationApplicationTests {

	@Test
	void saveDocTranslationTest() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
		String [] line1 = in.readLine().split(" ");
		int n = Integer.parseInt(line1[0]);
		int k = Integer.parseInt(line1[1]);
		String[] line2 = in.readLine().split(" ");

		int[] nums = new int[n];
		for(int i = 0; i < n; i++)
			nums[i] = Integer.parseInt(line2[i]);

		Deque<Integer> que = new LinkedList<Integer>();

		for(int i = 0; i< n; i++){
			if(!que.isEmpty() && i - k + 1 < que.peekFirst() )  que.removeFirst();

			while(!que.isEmpty() && nums[que.peekLast()] >= nums[i] )     que.removeLast();

			que.offerLast(i);
			out.write(nums[que.peekFirst()] + " ");

		}
		out.write("\n");
	}

}
