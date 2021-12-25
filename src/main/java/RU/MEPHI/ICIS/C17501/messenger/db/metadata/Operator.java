package RU.MEPHI.ICIS.C17501.messenger.db.metadata;

/**
 * Enum для предоставления доступа к различным типа операторов
 */
public enum Operator {

    /**
     * Операция внутри чата над сообщениями
     */
    STREAM("stream"),

    /**
     * Операция фильтрации сообщений внутри чата
     */
    FILTER("filter");

    /**
     * Тип операции
     */
    public String operatorType;

    Operator(String operatorType) {
        this.operatorType = operatorType;
    }
}
