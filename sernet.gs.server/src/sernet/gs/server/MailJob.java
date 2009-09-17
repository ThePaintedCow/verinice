package sernet.gs.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.log4j.Logger;
import org.eclipse.osgi.util.NLS;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.quartz.QuartzJobBean;

import sernet.gs.server.commands.NotificationInfo;
import sernet.gs.server.commands.PrepareNotificationInfo;
import sernet.gs.ui.rcp.main.bsi.model.MassnahmenUmsetzung;
import sernet.gs.ui.rcp.main.common.model.CnATreeElement;
import sernet.gs.ui.rcp.main.common.model.configuration.Configuration;
import sernet.gs.ui.rcp.main.service.ICommandService;
import sernet.gs.ui.rcp.main.service.commands.CommandException;

public class MailJob extends QuartzJobBean implements StatefulJob {
	
	private static final Logger log = Logger.getLogger(MailJob.class);
	
	private boolean notificationEnabled;
	
	private PrepareNotificationInfo pniCommand;
	
	private JavaMailSender mailSender;
	
	private ICommandService commandService;
	
	protected void executeInternal(JobExecutionContext ctx)
			throws JobExecutionException {
		if (!notificationEnabled)
			return;
		
		try {
			commandService.executeCommand(pniCommand);
		} catch (CommandException e) {
			throw new JobExecutionException("Exception when retrieving expiration information.", e); //$NON-NLS-1$
		}
		
		for (NotificationInfo ei : pniCommand.getExpirationInfo())
		{
			MessageHelper mh = new MessageHelper(ei.getConfiguration(), mailSender.createMimeMessage());
			
			try
			{
				if (ei.isCompletionExpired())
					mh.addCompletionExpirationEvent();
				
				if (ei.isRevisionExpired())
					mh.addRevisionExpirationEvent();
				
				for (MassnahmenUmsetzung mu : ei.getGlobalExpiredCompletions())
					mh.addCompletionExpirationEvent(mu);
				
				for (MassnahmenUmsetzung mu : ei.getGlobalExpiredRevisions())
					mh.addRevisionExpirationEvent(mu);
				
				for (MassnahmenUmsetzung mu : ei.getModifiedMeasures())
					mh.addMeasureModifiedEvent(mu);

				mailSender.send(mh.createMailMessage());
			}
			catch (MessagingException me)
			{
				log.warn("failed to create/send notification message: " + me); //$NON-NLS-1$
			}
			
		}
		
	}
	
	public void setCommandService(ICommandService commandService) {
		this.commandService = commandService;
	}

	public void setPniCommand(PrepareNotificationInfo pniCommand) {
		this.pniCommand = pniCommand;
	}

	public void setNotificationEnabled(boolean notificationEnabled) {
		this.notificationEnabled = notificationEnabled;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	private static class MessageHelper
	{
		String to;
		
		List<String> events = new ArrayList<String>();
		
		Map<CnATreeElement, List<String>> globalExpirationEvents = new HashMap<CnATreeElement, List<String>>();

		Map<CnATreeElement, List<String>> measureModificationEvents = new HashMap<CnATreeElement, List<String>>();
		
		MimeMessage mm;
		
		MessageHelper(Configuration recipient, MimeMessage mm)
		{
			this.to = recipient.getNotificationEmail();
			this.mm = mm;
		}
		
		void addCompletionExpirationEvent()
		{
			events.add(MailMessages.MailJob_1);
		}
		
		void addRevisionExpirationEvent()
		{
			events.add(MailMessages.MailJob_2);
		}
		
		void addCompletionExpirationEvent(MassnahmenUmsetzung mu)
		{
			CnATreeElement cte = mu.getParent().getParent();
			List<String> l = globalExpirationEvents.get(cte);
			if (l == null)
			{
				l = new ArrayList<String>();
				globalExpirationEvents.put(cte, l);
			}
			
			l.add(NLS.bind(MailMessages.MailJob_3, mu.getTitel()));
		}
		
		void addRevisionExpirationEvent(MassnahmenUmsetzung mu)
		{
			CnATreeElement cte = mu.getParent().getParent();
			List<String> l = globalExpirationEvents.get(cte);
			if (l == null)
			{
				l = new ArrayList<String>();
				globalExpirationEvents.put(cte, l);
			}
			
			l.add(NLS.bind(MailMessages.MailJob_4, mu.getTitel()));
		}
		
		void addMeasureModifiedEvent(MassnahmenUmsetzung mu)
		{
			CnATreeElement cte = mu.getParent().getParent();
			List<String> l = measureModificationEvents.get(cte);
			if (l == null)
			{
				l = new ArrayList<String>();
				measureModificationEvents.put(cte, l);
			}
			
			l.add("\t" + mu.getTitel() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		MimeMessage createMailMessage() throws MessagingException
		{
			mm.setFrom(new InternetAddress(MailMessages.MailJob_5));
			mm.setRecipient(RecipientType.TO, new InternetAddress(this.to));
			
			mm.setSubject(MailMessages.MailJob_6);
			
			StringBuffer sb = new StringBuffer();
			sb.append(MailMessages.MailJob_7);
			
			for (String evt : events)
			{
				sb.append(NLS.bind(MailMessages.MailJob_8, evt));
			}
			
			for (Map.Entry<CnATreeElement, List<String>> e : globalExpirationEvents.entrySet())
			{
				sb.append(NLS.bind(MailMessages.MailJob_9, e.getKey().getTitel()));
				for (String s : e.getValue())
					sb.append(s);
			}
			
			for (Map.Entry<CnATreeElement, List<String>> e : measureModificationEvents.entrySet())
			{
				sb.append(NLS.bind(MailMessages.MailJob_10, e.getKey().getTitel()));
				for (String s : e.getValue())
					sb.append(s);
			}
			
			sb.append("\n"); //$NON-NLS-1$
			
			sb.append(MailMessages.MailJob_11);
			
			mm.setText(sb.toString());
			
			return mm;
		}
	}
	
}
