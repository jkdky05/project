package ucab.ingsw.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString

public class Media {


        @Id
        private long id;

        private long identificador;
        private String url;


        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getIdentificador() {
            return identificador;
        }

        public void setIdentificador(long identificador) {
            this.identificador = identificador;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
}
