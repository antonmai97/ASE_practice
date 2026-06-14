#include <sys/socket.h>  /* struct sockaddr, socket, bind, listen, accept, AF_INET, SOCK_STREAM */
#include <netinet/in.h>  /* struct sockaddr_in, IPPROTO_TCP, INADDR_ANY */
#include <string.h>      /* strlen, memset */
#include <unistd.h>      /* write, sleep, close */
#include <stdio.h>       /* printf */

int main(int argc, char **argv) {
    // Initialize socket (file) handles and constants
    int listen_fd = 0, conn_fd = 0, server_port = 5000;
    char response_buffer[128] = "HTTP/1.1 200 OK\n"
                                "Content-Length: 12\n"
                                "Content-Type: text/html\n"
                                "\n"
                                "Hello World!";
    struct sockaddr_in server_addr;
    socklen_t addr_len = sizeof(server_addr);

    // Clear structure
    memset(&server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = htonl(INADDR_ANY); // converts unsigned int to network byte order
    server_addr.sin_port = htons(server_port); // converts unsigned short int to network byte order

    // 2. Create a socket using the socket() system call
    listen_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (listen_fd < 0) {
        perror("socket failed");
        return 1;
    }

    // Optional: Allow address reuse to avoid "Address already in use" errors during restarts
    int opt = 1;
    setsockopt(listen_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));

    // 3. Bind the socket to an address using the bind() system call
    if (bind(listen_fd, (struct sockaddr *)&server_addr, addr_len) < 0) {
        perror("bind failed");
        close(listen_fd);
        return 1;
    }

    // 4. Listen for connections using the listen() system call
    if (listen(listen_fd, 10) < 0) {  // Max queue length: 10
        perror("listen failed");
        close(listen_fd);
        return 1;
    }

    printf("Server läuft auf Port %d...\n", server_port);

    // 5. Endless loop: invoke accept() to block until a client connects
    while (1) {
        // Accept incoming connection
        conn_fd = accept(listen_fd, (struct sockaddr *)&server_addr, &addr_len);
        if (conn_fd < 0) {
            perror("accept failed");
            continue;
        }

        printf("Verbindung von Client empfangen.\n");

        // 6. Write the response buffer back to the client
        write(conn_fd, response_buffer, strlen(response_buffer));
        printf("Antwort gesendet.\n");

        // Close the socket and sleep for a moment
        close(conn_fd);
        sleep(1);
    }

    // This part is never reached due to the endless loop
    close(listen_fd);
    return 0;
}