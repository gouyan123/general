```text
流Stream遵循了"做什么而非怎么做"，将操作的具体调度交给具体实现去解决；
流Stream与集合Collection区别？
1、流Stream并不存储集合Collection的元素；这些元素可能存储在底层的数据集合中，或者是按需生成的；
2、流Stream的操作不会修改数据源Collection；例如，filter()方法不会从新的流Stream中移除数据，而是会生成一个新的流，其中不包含被过滤掉的元素；
3、流Stream的操作是尽可能惰性执行的，意味着，直到需要其结果时，操作才会执行；
总结：流Stream本身不存储源数据Collection，不修改源数据，只是存源数据的描述信息，即源数据Collection的元数据，只有调用触发算子时才会真正进行操作，按Stream里面的元数据描述去操作源数据Collection；
```

```text
流Stream操作流程：
1、创建一个流Stream：Stream stream = Stream.of(collection);
2、利用转换算子，将初始流转换成其他中间流Stream()；
3、利用触发算子，让操作真正执行；
```

