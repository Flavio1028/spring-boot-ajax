package com.codeup.web.dwr;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.codeup.repository.PromocaoRepository;

@Component
@RemoteProxy
public class DWRAlertaPromocoes {

	@Autowired
	private PromocaoRepository repository;

	private Timer timer;

	@RemoteMethod
	public synchronized void init() {
		System.out.println("DWR esta ativado!");

		LocalDateTime lastDate = this.getDtCadastroByUltimaPromocao();

		WebContext context = WebContextFactory.get();

		timer = new Timer();
		timer.schedule(new AlertTask(context, lastDate), 10000, 60000);

	}

	class AlertTask extends TimerTask {

		private WebContext context;

		private LocalDateTime lastDate;

		private Long count;

		public AlertTask(WebContext context, LocalDateTime lastDate) {
			this.context = context;
			this.lastDate = lastDate;
		}

		@Override
		public void run() {
			String session = context.getScriptSession().getId();

			Browser.withSession(context, session, new Runnable() {
				@Override
				public void run() {
					Map<String, Object> map = repository.totalAndUltimaPromocaoByDataCadastro(lastDate);
					count = (Long) map.get("count");
					lastDate = map.get("lastDate") == null ? lastDate : (LocalDateTime) map.get("lastDate");

					Calendar timer = Calendar.getInstance();
					timer.setTimeInMillis(context.getScriptSession().getLastAccessedTime());
					System.out.println("count: " + count + ", lastDate: " + lastDate + "<" + session + "> " + " <"
							+ timer.getTime() + ">");

					if (count > 0) {
						ScriptSessions.addFunctionCall("showButton", count);
					}
				}
			});
		}
	}

	private LocalDateTime getDtCadastroByUltimaPromocao() {
		PageRequest pageRequest = PageRequest.of(0, 1, Direction.DESC, "dtCadastro");
		return this.repository.findUltimaDataPromocao(pageRequest).getContent().get(0);
	}

}