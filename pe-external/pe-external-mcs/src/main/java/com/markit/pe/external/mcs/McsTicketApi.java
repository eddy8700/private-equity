package com.markit.pe.external.mcs;

import com.marketxs.userxs.session.objects.interfaces.AccountTicket;

public interface McsTicketApi {
	
	public AccountTicket validateTicket(final String TicketId);

}
