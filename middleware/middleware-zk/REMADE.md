middleware-middleware-zk:


curator示例代码：

https://github.com/apache/curator/tree/apache.curator-5.1.0/curator-examples/src/main/java


```
orSetData()方法：如果节点存在则Curator将会使用给出的数据设置这个节点的值，相当于 setData() 方法
creatingParentContainersIfNeeded()方法：如果指定节点的父节点不存在，则Curator将会自动级联创建父节点
guaranteed()方法：如果服务端可能删除成功，但是client没有接收到删除成功的提示，Curator将会在后台持续尝试删除该节点
deletingChildrenIfNeeded()方法：如果待删除节点存在子节点，则Curator将会级联删除该节点的子节点
```
