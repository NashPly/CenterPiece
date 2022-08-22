FROM openjdk:11
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN javac CenterPiece.java
CMD ["java", "Main"]
