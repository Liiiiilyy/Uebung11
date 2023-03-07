# java/Uebung11

为了测试代码，您需要复制整个项目，这样您就可以从BlueJ的两个不同实例中启动游戏两次。

任务1：遥控（40％）

从Player类中继承一个ControlledPlayer类。在其构造函数中，它应该打开一个服务器套接字并接受一个连接，以便通过此连接可以远程控制此类的一个实例。

- **socket**就是ip+port封装，作为一个方法供实例调用

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/31d9a21f-aae0-4dc7-9ca9-47f5f1689da7/Untitled.png)

- **ServerSocket 在client/server通信模式中, server端需要创建监听端口的 ServerSocket, ServerSocket 负责接收client连接请求.**
- **ServerSocket Constructor**
    
    ```java
    ServerSocket() throws IOException
    ServerSocket(int port) throws IOException
    
    //通过该方法创建的 ServerSocket 不与任何端口绑定,
    // 接下来还需要通过 bind() 方法与特定端口绑定.
    ServerSocket server = new ServerSocket();
    server.bind(new InetSocketAddress(port));
    //如果队列中没有连接请求, accept() 方法就会一直等待, 直到接收到了连接请求才返回.
          socket = server.accept();
    ```
    
- 重写act方法，以便它从**ServerSocket**读取运动方向并执行该方向。
    
    ```java
    @Override
        void act()
        {
            try {
                final int direction = socket.getInputStream().read();
                if (direction == -1) {
                    System.err.println("Verbindung wurde beendet.");
                    setVisible(false);
                }
                else {
                    setRotation(direction);
                    final int[][] offsets = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
                    setLocation(getX() + offsets[getRotation()][0], getY() + offsets[getRotation()][1]);
                    playSound("step");
                    sleep(200);
                }
            }
            catch (final IOException e) {
                System.err.println("Kann Bewegung nicht empfangen.");
                setVisible(false);
            }
        }
    ```
    
- 覆盖GameObject中的setVisible方法，并在角色变为不可见时关闭套接字。
    
    ```java
    public void setVisible(final boolean visible)
        {
            super.setVisible(visible);
            if (!visible && socket != null) {
                try {
                    socket.close();
                }
                catch (final IOException e) {
                    // Ignorieren
                }
            }
        }
    ```
    

任务2：远程控制（40％）

从Player类中继承一个RemotePlayer类。在其构造函数中，它应打开到ControlledPlayer类实例的套接字。然后，重写act方法，以便它首先执行Player中的act方法，然后将所选的移动传输到ControlledPlayer。在发送后，调用flush（）方法以确保数据立即发送。对于关闭套接字的方法，可以采用与任务1类似的方法。

```java
@Override
    void act()
    {
        super.act();
        try {
            socket.getOutputStream().write(getRotation());
            socket.getOutputStream().flush();
        }
        catch (final IOException e) {
            System.err.println("Kann Bewegung nicht senden.");
            setVisible(false);
        }
    }
```

任务3：游戏（20％）

修改您的Level类，使其可以选择创建ControlledPlayer或RemotePlayer作为游戏角色。此外，扩展游戏的主方法，以便您可以传递要创建的选项。例如，可以传递IP地址（或不传递地址以用于ControlledPlayer）和端口号。

- `Level(final String fileName, final String address, final int port)`

```java
final Player player = address == null
                ? new ControlledPlayer(-1, 0, 0, field, port)
                : new RemotePlayer(-1, 0, 0, field, address, port);
```

```java
int a = 3>1 ? 100 : 0;

if(3>1){
	a = 100;
}
else a= 0;
```

- 在主类中

`final Level level = new Level("levels/1.lvl", address, port);`
