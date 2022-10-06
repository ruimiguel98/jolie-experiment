FROM jolielang/jolie

# Set the working directory to /app
WORKDIR /app/myapp

# Copy the current directory contents into the container at /app
ADD . /app/myapp

# Install any needed packages (PostgreSQL JDBC)
RUN wget https://jdbc.postgresql.org/download/postgresql-42.4.0.jar
RUN mv postgresql-42.4.0.jar jdbc-postgresql.jar
RUN cp jdbc-postgresql.jar /usr/lib/jolie/lib/jdbc-postgresql.jar

# Make port 9051 available to the world outside this container
EXPOSE 9051

# Run ProductService.ol when the container launches
CMD jolie ./ProductService.ol 