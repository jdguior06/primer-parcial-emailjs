package coneccion.interfaces;

import coneccion.utils.Email;
import java.util.List;

/**
 *
 * @author  DIEGO
 */
public interface IEmailEventListener {
      void onReceiveEmailEvent(List<Email> emails);
}
