FROM openjdk:11
COPY . /src/main/java/com/CenterPiece
WORKDIR ./src/main/java/com/CenterPiece
RUN cd src/main/java/com/CenterPiece
RUN dir
CMD ["java", "Main"]
