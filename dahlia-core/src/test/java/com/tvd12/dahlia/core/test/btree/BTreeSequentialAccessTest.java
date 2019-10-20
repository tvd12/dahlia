package com.tvd12.dahlia.core.test.btree;

import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.Test;

import com.tvd12.dahlia.core.Tree;
import com.tvd12.dahlia.core.TreeWalker;
import com.tvd12.dahlia.core.btree.BTree;

public class BTreeSequentialAccessTest {

	@Test
	public void test() {
		BTree<Integer, Integer> tree = new BTree<>(4);
		System.out.println("sequential access 1st:\n");
		tree.walk(e ->  {
			System.out.print(e + " ");
		});
		for(int i = 1 ; i <= 100 ; ++ i)
			tree.insert(i, i);
		
		System.out.println("\nsequential access 2nd:\n");
		tree.walk(e ->  {
			System.out.print(e + " ");
		});
		
		System.out.println("\nsequential access 3rd:\n");
		for(int i = 1 ; i <= 100 ; ++ i) {
			final int index = i;
			tree.walk(new TreeWalker<Integer, Integer>() {
				
				AtomicInteger count = new AtomicInteger();
				
				@Override
				public void accept(Tree.Entry<Integer, Integer> e) {
					System.out.print(e + " ");
					count.incrementAndGet();
				}
				
				@Override
				public boolean next() {
					return count.get() < index;
				}
			});
			System.out.println();
		}
	}
	
}
